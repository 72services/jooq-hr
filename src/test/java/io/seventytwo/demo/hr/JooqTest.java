package io.seventytwo.demo.hr;

import io.seventytwo.demo.hr.dto.DepartmentSalaryStatistics;
import io.seventytwo.demo.hr.model.tables.records.EmployeeRecord;
import io.seventytwo.demo.hr.model.tables.records.PhoneRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.XML;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.seventytwo.demo.hr.model.tables.Address.ADDRESS;
import static io.seventytwo.demo.hr.model.tables.Department.DEPARTMENT;
import static io.seventytwo.demo.hr.model.tables.Employee.EMPLOYEE;
import static io.seventytwo.demo.hr.model.tables.Phone.PHONE;
import static io.seventytwo.demo.hr.model.tables.ProjectEmployees.PROJECT_EMPLOYEES;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlelement;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JooqTest {

    @Autowired
    private DSLContext dsl;

    /**
     * Ex1	Find all employees who live in the canton of Zurich
     */
    @Test
    public void findAllZuercher() {
        Result<Record> list = dsl
                .select()
                .from(EMPLOYEE)
                .join(ADDRESS).on(ADDRESS.ID.eq(EMPLOYEE.ADDRESS_ID))
                .where(ADDRESS.STATE.eq("ZH"))
                .fetch();

        assertEquals(3, list.size());
    }

    /**
     * Ex2	Calculate the average salary of employees per department
     */
    @Test
    public void getAverageSalaryPerDepartment() {
        List<DepartmentSalaryStatistics> list = dsl
                .select(DEPARTMENT.NAME, avg(EMPLOYEE.SALARY))
                .from(DEPARTMENT)
                .join(EMPLOYEE).on(EMPLOYEE.DEPARTMENT_ID.eq(DEPARTMENT.ID))
                .groupBy(DEPARTMENT.NAME)
                .fetchInto(DepartmentSalaryStatistics.class);

        assertEquals(2, list.size());

        for (DepartmentSalaryStatistics statistics : list) {
            if (statistics.departmentName().equals("IT")) {
                assertEquals(97200.0, statistics.avgSalary(), 0);
            }
            if (statistics.departmentName().equals("HR")) {
                assertEquals(95000.0, statistics.avgSalary(), 0);
            }
        }
    }

    /**
     * Ex3	Find the employee with the lowest salary
     */
    @Test
    public void findEmployeeWithSmallestSalary() {
        Result<EmployeeRecord> list = dsl
                .selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.eq(dsl
                        .select(min(EMPLOYEE.SALARY))
                        .from(EMPLOYEE))
                )
                .fetch();

        assertEquals(2, list.size());

        assertEquals("Luca Traugott", list.get(0).getName());
        assertEquals("Lea Schulze", list.get(1).getName());
    }

    /**
     * Ex4 	Create a query that returns the employee name and the complete address, ordered by the employee’s name
     */
    @Test
    public void findAllEmployeeNameWithAddress() {
        Result<Record2<String, String>> list = dsl
                .select(EMPLOYEE.NAME, ADDRESS.CITY)
                .from(EMPLOYEE)
                .join(ADDRESS).on(ADDRESS.ID.eq(EMPLOYEE.ADDRESS_ID))
                .fetch();

        assertEquals(6, list.size());
    }

    /**
     * Ex5  Find employees who are not assigned to a project
     */
    @Test
    public void findAllEmployeesWithoutProject() {
        Result<Record> list = dsl
                .select()
                .from(EMPLOYEE)
                .whereNotExists(dsl
                        .selectFrom(PROJECT_EMPLOYEES)
                        .where(PROJECT_EMPLOYEES.EMPLOYEES_ID.eq(EMPLOYEE.ID))
                )
                .fetch();
        assertEquals(3, list.size());
    }

    /**
     * Ex6	Find all business phone numbers ordered by number
     */
    @Test
    public void findAllWorkPhonesOrderedByNumber() {
        Result<PhoneRecord> list = dsl
                .selectFrom(PHONE)
                .where(PHONE.TYPE.eq("WORK"))
                .orderBy(PHONE.PHONENUMBER)
                .fetch();
        assertEquals(5, list.size());
    }


    /**
     * Ex 7 Find employees who do not have a business phone number yet
     */
    @Test
    public void findAllEmployeesWithoutWorkPhone() {
        Result<Record2<Integer, Integer>> list = dsl
                .select(EMPLOYEE.ID, PHONE.ID)
                .from(EMPLOYEE)
                .leftOuterJoin(PHONE).on(EMPLOYEE.ID.eq(PHONE.EMPLOYEE_ID))
                .where(PHONE.ID.isNull())
                .fetch();

        assertEquals(1, list.size());
    }

    /**
     * XML_ELEMENT
     */
    @Test
    public void xml() {
        Field<XML> phone = dsl
                .select(xmlagg(xmlelement("phone", xmlattributes(PHONE.PHONENUMBER, PHONE.TYPE))))
                .from(PHONE)
                .where(PHONE.EMPLOYEE_ID.eq(EMPLOYEE.ID))
                .asField();

        String xml = dsl
                .select(xmlelement("employees",
                        xmlagg(xmlelement("employee", xmlattributes(EMPLOYEE.ID, EMPLOYEE.NAME), xmlelement("phones", phone)))))
                .from(EMPLOYEE)
                .fetchOneInto(String.class);

        System.out.println(xml);
    }

    @Test
    public void json() {
        Field<JSON> employee = DSL.field("employee", JSON.class);

        Table<Record1<JSON>> employees = dsl
                .select(jsonObject(jsonEntry("id", EMPLOYEE.ID), jsonEntry("name", EMPLOYEE.NAME), jsonEntry("phones",
                        jsonArrayAgg(
                                jsonObject(jsonEntry("number", PHONE.PHONENUMBER), jsonEntry("type", PHONE.TYPE)))
                )).as(employee))
                .from(EMPLOYEE)
                .join(PHONE).on(PHONE.EMPLOYEE_ID.eq(EMPLOYEE.ID))
                .groupBy(EMPLOYEE.ID)
                .asTable();

        String json = dsl
                .select(jsonArrayAgg(employees.field(employee)))
                .from(employees)
                .fetchOneInto(String.class);

        System.out.println(json);
    }
}
