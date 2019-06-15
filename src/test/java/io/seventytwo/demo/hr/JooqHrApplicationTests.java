package io.seventytwo.demo.hr;

import io.seventytwo.demo.hr.dto.EmployeeDTO;
import io.seventytwo.demo.hr.dto.EmployeeWithAddressDTO;
import io.seventytwo.demo.hr.model.tables.records.AddressRecord;
import io.seventytwo.demo.hr.model.tables.records.DepartmentRecord;
import io.seventytwo.demo.hr.model.tables.records.EmployeeRecord;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.seventytwo.demo.hr.model.tables.Address.ADDRESS;
import static io.seventytwo.demo.hr.model.tables.Department.DEPARTMENT;
import static io.seventytwo.demo.hr.model.tables.Employee.EMPLOYEE;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class JooqHrApplicationTests {

    @Autowired
    private DSLContext create;

    private int departmentId;
    private int mangerId;
    private int employeeId;
    private int addressId;

    @Before
    public void insertData() {
        DepartmentRecord department = new DepartmentRecord(null, "IT");
        create.attach(department);
        department.store();

        departmentId = department.getId();

        EmployeeRecord manager = new EmployeeRecord(null, "Gates", "Bill", 100000, departmentId, null);
        create.attach(manager);
        manager.store();

        mangerId = manager.getId();

        EmployeeRecord employee = new EmployeeRecord(null, "Doe", "John", 80000, departmentId, mangerId);
        create.attach(employee);
        employee.store();

        employeeId = employee.getId();

        AddressRecord address = new AddressRecord(null, "27 Beverly Park Terrace", "90210", "Beverly Hills", employeeId);
        create.attach(address);
        address.store();

        addressId = address.getId();
    }

    @Test
    public void query() {
        DepartmentRecord departmentRecord = create
                .selectFrom(DEPARTMENT)
                .where(DEPARTMENT.NAME.eq("IT"))
                .fetchOne();

        assertNotNull(departmentRecord);
        assertEquals("IT", departmentRecord.getName());
    }

    @Test
    public void insert() {
        int affectedRows = create
                .insertInto(DEPARTMENT)
                .columns(DEPARTMENT.NAME)
                .values("HR")
                .execute();

        assertEquals(1, affectedRows);
    }

    @Test
    public void update() {
        int affectedRows = create
                .update(DEPARTMENT)
                .set(DEPARTMENT.NAME, "IT2")
                .where(DEPARTMENT.ID.eq(departmentId))
                .execute();

        assertEquals(1, affectedRows);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void delete() {
        int affectedRows = create
                .deleteFrom(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(employeeId))
                .execute();
    }

    @Test
    public void storeRecord() {
        DepartmentRecord departmentRecord = new DepartmentRecord(null, "IT");
        create.attach(departmentRecord);
        departmentRecord.store();

        assertEquals(2, departmentRecord.getId().intValue());
    }

    @Test
    public void join() {
        List<EmployeeRecord> list = create
                .select()
                .from(EMPLOYEE)
                .join(DEPARTMENT).on(DEPARTMENT.ID.eq(EMPLOYEE.DEPARTMENT_ID))
                .where(DEPARTMENT.NAME.eq("IT"))
                .fetchInto(EmployeeRecord.class);

        assertEquals(2, list.size());
    }

    @Test
    public void employeeWithAddressDtoProjection() {
        List<EmployeeWithAddressDTO> list = create
                .select(EMPLOYEE.LASTNAME, EMPLOYEE.FIRSTNAME, ADDRESS.STREET, ADDRESS.ZIP, ADDRESS.CITY)
                .from(EMPLOYEE)
                .join(ADDRESS).on(ADDRESS.EMPLOYEE_ID.eq(EMPLOYEE.ID))
                .fetchInto(EmployeeWithAddressDTO.class);

        assertEquals(1, list.size());

        EmployeeWithAddressDTO dto = list.get(0);
        assertEquals("90210", dto.getAddressZip());
    }

    @Test
    public void employeeDtoProjection() {
        List<EmployeeDTO> records = create
                .select(EMPLOYEE.LASTNAME, EMPLOYEE.FIRSTNAME, EMPLOYEE.SALARY)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.between(80000, 100000))
                .fetchInto(EmployeeDTO.class);

        assertEquals(2, records.size());
    }

    @Test
    public void plainSql() {
        Result<Record> records = create
                .fetch("WITH RECURSIVE employeetree(id, lastname, firstname, manager_id) AS (" +
                        "SELECT id, lastname, firstname, manager_id FROM employee WHERE manager_id IS NULL " +
                        "UNION ALL " +
                        "SELECT employee.id, employee.lastname, employee.firstname, employee.manager_id FROM employeetree INNER JOIN employee ON (employeetree.id = employee.manager_id) " +
                        ") " +
                        "SELECT id, lastname, firstname, manager_id FROM employeetree");

        assertEquals(2, records.size());
    }

    @Test
    public void withRecursive() {
        Name employeetree = name("employeetree");

        Result<Record> records = create
                .withRecursive(employeetree, EMPLOYEE.ID.getUnqualifiedName(), EMPLOYEE.LASTNAME.getUnqualifiedName(), EMPLOYEE.FIRSTNAME.getUnqualifiedName(), EMPLOYEE.MANAGER_ID.getUnqualifiedName())
                .as(
                        select(EMPLOYEE.ID, EMPLOYEE.LASTNAME, EMPLOYEE.FIRSTNAME, EMPLOYEE.MANAGER_ID).from(EMPLOYEE).where(EMPLOYEE.MANAGER_ID.isNull())
                                .unionAll(
                                        select(EMPLOYEE.ID, EMPLOYEE.LASTNAME, EMPLOYEE.FIRSTNAME, EMPLOYEE.MANAGER_ID)
                                                .from(employeetree).innerJoin(EMPLOYEE).on("{0}.ID = {1}", employeetree, EMPLOYEE.MANAGER_ID))
                )
                .selectFrom(employeetree)
                .fetch();

        assertEquals(2, records.size());
    }

}
