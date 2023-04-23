package modelo.servicio.account;

import modelo.Account;

import java.util.List;

public interface IServicioAccount {
    long createAccount(Account cuenta);

    List<Account> findByEmpno(int empno);

    List<Object> getValues();
}
