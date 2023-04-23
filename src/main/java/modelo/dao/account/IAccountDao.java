package modelo.dao.account;

import modelo.Account;
import modelo.dao.IGenericDao;

import java.util.List;

public interface IAccountDao extends IGenericDao<Account> {
    List<Account> getAccountsByEmpno(Integer empno);

    List<Object> getValues();

    List<Object[]> getValues2();


}
