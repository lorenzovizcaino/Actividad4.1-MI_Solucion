/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao.empleado;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.neodatis.odb.*;
import org.neodatis.odb.core.oid.OIDFactory;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.IValuesQuery;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import modelo.Empleado;
import modelo.dao.AbstractGenericDao;
import modelo.exceptions.InstanceNotFoundException;
import util.ConnectionFactory;

/**
 *
 * @author mfernandez
 */
public class EmpleadoNeoDatisDao extends AbstractGenericDao<Empleado> implements IEmpleadoDao {

	private ODB dataSource;

	public EmpleadoNeoDatisDao() {
		this.dataSource = ConnectionFactory.getConnection();
	}

	@Override
	public long create(Empleado entity) {
		OID oid = null;
		long oidlong =-1;
		try {
			
			oid = this.dataSource.store(entity);
			this.dataSource.commit();

		} catch (Exception ex) {
			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
			this.dataSource.rollback();
			oid = null;
		}
		if(oid!=null) {
			oidlong= oid.getObjectId();
		}
		return oidlong;
	}
	@Override
	public boolean delete(Empleado entity) {
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
	public Empleado read(long id) throws InstanceNotFoundException {
		Empleado empleado = null;
		try {
			OID oid = OIDFactory.buildObjectOID(id);
			empleado = (Empleado) this.dataSource.getObjectFromId(oid);
		} catch (ODBRuntimeException ex) {
		
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
//Suponemos que no lo encuentra
			throw new InstanceNotFoundException(id, getEntityClass());
		}
		catch(Exception ex) {
			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

		}
		return empleado;
	}

	@Override
	public boolean update(Empleado entity) {
		boolean exito = false;
		try {
			this.dataSource.store(entity);
			this.dataSource.commit();
			exito = true;
		} catch (Exception ex) {			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
			this.dataSource.rollback();
			

		}
		return exito;																	// nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}





	

	@Override
	public float findAvgSalary() {
		BigDecimal media =BigDecimal.ZERO;
		ValuesCriteriaQuery valuesCriteriaQuery = new ValuesCriteriaQuery(Empleado.class);
		IValuesQuery ivc = valuesCriteriaQuery.avg("sal");
		Values values = this.dataSource.getValues(ivc);
		while(values.hasNext()) {
			ObjectValues objectValues = (ObjectValues) values.next();			
			media = (BigDecimal) objectValues.getByIndex(0); 
			
		}
		return media.floatValue();
	}

	




	@Override
	public List<Empleado> findAll() {
		List <Empleado> empleados=new ArrayList<>();
		//Sin ordenar
		//Objects <Empleado> emp=dataSource.getObjects(Empleado.class);
		//si los quisieramos ordenados por algun campo
		CriteriaQuery query = new CriteriaQuery(Empleado.class);
		IQuery iquery = query.orderByAsc("empno");
		Objects<Empleado> emp = dataSource.getObjects(iquery);

		for (Empleado obj:emp) {
			empleados.add(obj);
		}

		return empleados;
	}

	@Override
	public List<Empleado> findByJob(String puesto) {
		List <Empleado> empleados=new ArrayList<>();
		IQuery iquery=new CriteriaQuery(Empleado.class, Where.equal("job",puesto));
		//tambien valido
		//CriteriaQuery query = new CriteriaQuery(Empleado.class, Where.equal("job", puesto));
		Objects<Empleado>emp=dataSource.getObjects(iquery);
		for (Empleado obj:emp) {
			empleados.add(obj);
		}

		return empleados;
	}

	@Override
	public boolean exists(Integer empno) {
		boolean exito=false;
		List <Empleado> empleados=new ArrayList<>();
		empleados=findAll();
		for (Empleado emp:empleados) {
			if(emp.getEmpno()==empno){
				exito=true;
				break;
			}
		}
		return exito;
		//otra forma de hacerlo:(Maria)
//		CriteriaQuery query = new CriteriaQuery(Empleado.class,	Where.equal("empno", empno));
//		Objects<Empleado> empleados = dataSource.getObjects(query);
//		return (empleados.size()==1);
	}

	@Override
	public List<Empleado> findEmployeesByHireDate(Date from, Date to) {
		List <Empleado> empleados=new ArrayList<>();
		ICriterion criterio=new And().add(Where.ge("hiredate",from))
				.add(Where.le("hiredate",to));
		CriteriaQuery query=new CriteriaQuery(Empleado.class,criterio);
		Objects<Empleado> emp = dataSource.getObjects(query);
		for (Empleado e:emp) {
			empleados.add(e);
		}
		return empleados;
	}
	
	

}
