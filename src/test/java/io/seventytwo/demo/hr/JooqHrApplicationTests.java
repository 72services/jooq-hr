package io.seventytwo.demo.hr;

import io.seventytwo.demo.hr.dto.EmployeeWithAddressDTO;
import io.seventytwo.demo.hr.model.tables.records.AddressRecord;
import io.seventytwo.demo.hr.model.tables.records.DepartmentRecord;
import io.seventytwo.demo.hr.model.tables.records.EmployeeRecord;
import org.jooq.DSLContext;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class JooqHrApplicationTests {

    @Autowired
    private DSLContext create;

    private int departmentId;
    private int employeeId;
    private int addressId;

    @Before
    public void insertData() {
        DepartmentRecord department = new DepartmentRecord(null, "IT");
        create.attach(department);
        department.store();

        departmentId = department.getId();

        EmployeeRecord employee = new EmployeeRecord(null, "John Doe", 80000, departmentId, null);
        create.attach(employee);
        employee.store();

        employeeId = employee.getId();

        AddressRecord address = new AddressRecord(null, "27 Beverly Park Terrace", "90210", "Beverly Hills", employeeId);
        create.attach(address);
        address.store();

        addressId = address.getId();
    }

    @Test
    public void select() {
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

        assertEquals(1, list.size());
    }

    @Test
    public void dtoProjection() {
        List<EmployeeWithAddressDTO> list = create
                .select(EMPLOYEE.NAME, ADDRESS.STREET, ADDRESS.ZIP, ADDRESS.CITY)
                .from(EMPLOYEE)
                .join(ADDRESS).on(ADDRESS.EMPLOYEE_ID.eq(EMPLOYEE.ID))
                .fetchInto(EmployeeWithAddressDTO.class);

        assertEquals(1, list.size());

        EmployeeWithAddressDTO dto = list.get(0);
        assertEquals("90210", dto.getAddressZip());
    }

}
