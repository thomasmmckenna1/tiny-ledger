# tiny-ledger

## Requirements

- **Java 17+**
- **Maven 3.6+**

## Running

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

To run tests:

```bash
./mvnw test
```

## Endpoints

A Postman collection is included: [tiny-ledger.postman_collection.json](tiny-ledger.postman_collection.json)

### Accounts

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/accounts` | Create a new account |
| `GET` | `/accounts` | List all accounts |
| `GET` | `/accounts/{accountId}/balance` | Get account balance |

### Transactions

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/accounts/{accountId}/transactions` | Deposit or withdraw |
| `GET` | `/accounts/{accountId}/transactions` | Get transaction history |

### Request body — POST `/accounts/{accountId}/transactions`

```json
{
  "transactionType": "DEPOSIT",
  "amount": 100.00
}
```

`transactionType` is either `DEPOSIT` or `WITHDRAW`.
