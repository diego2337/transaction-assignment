# transaction-assignment
Assignment to create a simple transaction API, given a set of rules. Description of assignment kept under wraps.

## Collection
[Transaction assignment collection (Postman, Insomnia)](public/transactionAssignment.json).

## (pt-BR) - L4

Segue a resposta para a seguinte pergunta:

> Transações simultâneas: dado que o mesmo cartão de crédito pode ser utilizado em diferentes serviços online, existe uma pequena mas existente probabilidade de ocorrerem duas transações ao mesmo tempo. O que você faria para garantir que apenas uma transação por conta fosse processada em um determinado momento? Esteja ciente do fato de que todas as solicitações de transação são síncronas e devem ser processadas rapidamente (menos de 100 ms), ou a transação atingirá o timeout.

Existem algumas abordagens para resolver esse problema. A seguir, elencam-se duas.

### Travamento otimista

Pode-se utilizar essa técnica para garantir que apenas uma transação por conta fosse processada em um determinado momento. Para isso, inicialmente:

1. Adiciona-se na tabela `account` uma coluna `version`(versão).
2. Antes de uma conta modificar a tupla dessa coluna, a aplicação verifica sua versão.
3. Quando a transação é concluída, a aplicação incrementa a versão (+1) e escreve de volta na base.
4. Com isso, uma validação na base de dados é criada. A próxima versão tem que exceder em +1, pelo menos. Se uma conta ler essa mesma tupla, mas com a versão anterior, então a transação é cancelada, e volta-se ao passo 2.

Segue-se um exemplo modificado do código `Account.java` para incluir a versão:

```
package com.transactionAssignment.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "account")
@Slf4j
public class Account {
    @Id
    private String id;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountCategory> accountCategories;
    
    @Version
    private Long version;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Optional<AccountCategory> findCategoryByMccs(List<String> mccs) {
        return accountCategories.stream()
                .filter(accountCategory -> accountCategory.getCategory().getMccCategories().stream()
                        .anyMatch(mccCategory -> mccs.contains(mccCategory.getMcc())))
                .findFirst();
    }
}
```

Assim, ao invocar o `accountRepository.save`, a anotação `@Version` inclusa vai garantir o controle de concorrência.

### Bloqueio distribuído

Outra maneira similar ao travamento é o bloqueio distribuído, considerando um cenário distribuído. Utilizando Redis, por exemplo, pode-se criar o seguinte mecanismo:

1. Quando uma requisição é recebida, tenta-se adquirir uma chave Redis para a conta.
2. Se a chave é adquirida, cria-se uma trava. Senão, retorna uma mensagem de erro ou tenta-se novamente depois de um período curto de tempo, voltando ao passo 2.
3. Com a trava, executa a transação.
4. Com a transação completa, libera-se a chave e a trava.

Segue-se um exemplo extremamente simplificado de uso do bloqueio distribuído com Java e Redis:

```
import redis.clients.jedis.Jedis;
import java.util.UUID;

public class DistributedLockingExample {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int LOCK_TIMEOUT = 100; // ms

    private Jedis jedis;

    public DistributedLockingExample() {
        jedis = new Jedis(REDIS_HOST, REDIS_PORT);
    }

    public boolean acquireLock(String lockKey, String lockValue, int timeout) {
        String result = jedis.set(lockKey, lockValue, "NX", "PX", timeout);
        return "OK".equals(result);
    }

    public void releaseLock(String lockKey, String lockValue) {
        String currentValue = jedis.get(lockKey);
        if (lockValue.equals(currentValue)) {
            jedis.del(lockKey);
        }
    }

    public void processTransaction(String accountId, double amount) {
        // Atualizar a conta com o que veio da requisição
    }

    public void handleLockFailure() {
        // Tratar com falha ou retentativa
    }

    public void executeTransaction(String accountId, double amount) {
        String lockKey = "lock:account:" + accountId;
        String lockValue = UUID.randomUUID().toString();

        if (acquireLock(lockKey, lockValue, LOCK_TIMEOUT)) {
            try {
                // Executa a transação
                processTransaction(accountId, amount);
            } finally {
                // Liberar a chave
                releaseLock(lockKey, lockValue);
            }
        } else {
            // Tratar a trava, com erro ou retentativa
            handleLockFailure();
        }
    }

    public static void main(String[] args) {
        DistributedLockingExample example = new DistributedLockingExample();
        example.executeTransaction("123456", 100.00);
    }
}
```

## TODO
- Caching.
- Create integration tests.
- Implement distributed locking using [Jedis](https://github.com/redis/jedis).
- Create a stress test to validate distributed locking using [JMeter](https://jmeter.apache.org/).
- Fix Dockerfile to run with database.
- Consider adding timestamps for `account_category` table.