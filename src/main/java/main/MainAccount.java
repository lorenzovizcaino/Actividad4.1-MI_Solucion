package main;

import modelo.Account;
import modelo.Empleado;
import modelo.servicio.account.ServicioAccount;
import modelo.servicio.empleado.ServicioEmpleado;

import java.util.List;

public class MainAccount {
    public static ServicioEmpleado servicioEmpleado=new ServicioEmpleado();
    public static ServicioAccount servicioAccount=new ServicioAccount();
    public static void main(String[] args) {
        crearCuentas();
        int empno=3;
        findByEmpno(empno);
        getValues();
    }

    private static void getValues() {
        System.out.println("Se han recuperado los siguientes valores:");

        for(Object o :servicioAccount.getValues()) {
            System.out.println("Se ha recuperado: " +o);

        }
    }

    private static void findByEmpno(int empno) {
        List <Account> cuentas=servicioAccount.findByEmpno(empno);
        System.out.println("Cuentas del empleado con empno: "+empno);
        for (Account acc:cuentas) {
            System.out.println(acc);
        }

    }

    private static void crearCuentas() {
        List<Empleado> empleados=servicioEmpleado.findAll();
        int count=0;
        for (Empleado emp:empleados) {
            addAccountEmpleado(emp, ++count);
            addAccountEmpleado(emp, ++count);
        }
    }

    private static void addAccountEmpleado(Empleado emp, int nCuenta) {
        float amount=(float)Math.random()*1000;
        Account cuenta=new Account(emp,amount);
        cuenta.setAccountno(nCuenta);
        emp.getAccounts().add(cuenta);
        servicioAccount.createAccount(cuenta);

    }
}
