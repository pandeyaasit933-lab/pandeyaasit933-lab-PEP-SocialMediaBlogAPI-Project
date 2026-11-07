package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Register account
    public Account registerAccount(Account account) {

        try {
            if (account == null ||
                account.username == null ||
                account.password == null) {
                return null;
            }

            if (account.username.trim().isEmpty()) {
                return null;
            }

            if (account.password.length() < 4) {
                return null;
            }

            if (accountDAO.getAccountByUsername(account.username) != null) {
                return null;
            }

            return accountDAO.createAccount(account);

        } catch (Exception e) {
            return null;
        }
    }

    // Login account
    public Account loginAccount(Account account) {

        try {
            if (account == null ||
                account.username == null ||
                account.password == null) {
                return null;
            }

            return accountDAO.getAccountByUsernameAndPassword(
                    account.username,
                    account.password
            );

        } catch (Exception e) {
            return null;
        }
    }
}