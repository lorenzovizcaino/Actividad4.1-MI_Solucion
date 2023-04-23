package modelo.dao.account;

import modelo.Account;
import modelo.Empleado;
import modelo.dao.AbstractGenericDao;
import modelo.exceptions.InstanceNotFoundException;
import org.neodatis.odb.*;
import org.neodatis.odb.core.oid.OIDFactory;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;
import util.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountNeoDatisDao extends AbstractGenericDao<Account> implements IAccountDao{
    private ODB dataSource;

    public AccountNeoDatisDao() {
        this.dataSource = ConnectionFactory.getConnection();
    }

    @Override
    public long create(Account entity) {
        OID oid = null;
        long oidlong =-1;
        try {
            oid = dataSource.store(entity);
            dataSource.commit();
        }
        catch(Exception e){
            System.err.println("Ha ocurrido una excepción: " + e.getMessage());
            this.dataSource.rollback();
            oid = null;
        }
        if(oid!=null){

            oidlong=oid.getObjectId();
        }
        return oidlong;
    }

    @Override
    public Account read(long id) throws InstanceNotFoundException {
        Account account=null;
        try{
            OID oid= OIDFactory.buildObjectOID(id);
            account=(Account) dataSource.getObjectFromId(oid);
        }catch (ODBRuntimeException e){
            System.err.println("Ha ocurridio una excepcion: "+e.getMessage());
            throw new InstanceNotFoundException(id,getEntityClass());
        }catch (Exception e){
            System.err.println("Ha ocurrido una excepcion: "+e.getMessage());
        }
        return account;
    }

    @Override
    public boolean update(Account entity) {
        boolean exito = false;
        try {
            this.dataSource.store(entity);
            this.dataSource.commit();
            exito = true;
        } catch (Exception ex) {
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
            this.dataSource.rollback();


        }
        return exito;
    }

    @Override
    public boolean delete(Account entity) {
        boolean exito=false;
        try{
            OID oid=this.dataSource.delete(entity);
            System.out.println("El OID del objeto eliminado es: "+oid.getObjectId());
            this.dataSource.commit();
            exito=true;
        }catch (Exception e){
            System.err.println("Ha ocurrido una excepcion: "+e.getMessage());
            this.dataSource.rollback();
        }


        return exito;
    }

    @Override
    public List<Account> findAll() {
        List <Account> cuentas=new ArrayList<>();
        CriteriaQuery query = new CriteriaQuery(Account.class);
        IQuery iquery = query.orderByAsc("accountno");
        Objects<Account> cuen = dataSource.getObjects(iquery);

        for (Account obj:cuen) {
            cuentas.add(obj);
        }

        return cuentas;
    }

    @Override
    public List<Account> getAccountsByEmpno(Integer empno) {
        List <Account> cuentas=new ArrayList<>();
        IQuery iQuery=new CriteriaQuery(Account.class, Where.equal("emp.empno",empno));
        Objects<Account>accounts=dataSource.getObjects(iQuery);
        for (Account acc:accounts) {
            cuentas.add(acc);
        }
        return cuentas;
    }

    @Override
    public List<Object> getValues() {
        List<Object> obj=new ArrayList<>();
        Values values=dataSource.getValues(new ValuesCriteriaQuery(Account.class)
                .field("emp.empno")
                .field("emp.ename")
                .field("amount")
                .field("accountno"));
        for (ObjectValues objectValues:values) {
            obj.add(objectValues);
        }
        return obj;
    }

    @Override
    public List<Object[]> getValues2() {
        return null;
    }
}
