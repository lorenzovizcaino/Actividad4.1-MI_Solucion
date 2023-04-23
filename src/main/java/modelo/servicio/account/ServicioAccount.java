package modelo.servicio.account;


import modelo.Account;
import modelo.dao.account.AccountNeoDatisDao;
import modelo.dao.account.IAccountDao;

import java.util.List;

public class ServicioAccount implements IServicioAccount {
    public static IAccountDao accountNeoDatisDao=new AccountNeoDatisDao();
    @Override
    public long createAccount(Account cuenta) {
        return accountNeoDatisDao.create(cuenta);
    }

    @Override
    public List<Account> findByEmpno(int empno) {
        return accountNeoDatisDao.getAccountsByEmpno(empno);
    }

    @Override
    public List<Object> getValues() {
        return accountNeoDatisDao.getValues();
    }
}
