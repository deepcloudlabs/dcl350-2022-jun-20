package com.example.hr.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hr.application.HrApplication;
import com.example.hr.application.business.exception.EmployeeNotFoundException;
import com.example.hr.domain.Employee;
import com.example.hr.domain.TcKimlikNo;
import com.example.hr.dto.request.HireEmployeeRequest;
import com.example.hr.dto.request.UpdateEmployeeSalaryRequest;
import com.example.hr.dto.response.EmployeeResponse;
import com.example.hr.dto.response.FireEmployeeResponse;
import com.example.hr.dto.response.HireEmployeeResponse;
import com.example.hr.dto.response.UpdateEmployeeSalaryResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;

@Service
public class HrService {
	private HrApplication hrApplication;
	private ModelMapper modelMapper;

	public HrService(HrApplication hrApplication, ModelMapper modelMapper) {
		this.hrApplication = hrApplication;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public HireEmployeeResponse hireEmployee(HireEmployeeRequest request) {
		var employee = modelMapper.map(request, Employee.class);
		hrApplication.hireEmployee(employee);
		return new HireEmployeeResponse("success");
	}

	@Transactional
	public FireEmployeeResponse fireEmployee(String identity) {
		TcKimlikNo kimlik = TcKimlikNo.valueOf(identity);
		var firedEmployee = hrApplication.fireEmployee(kimlik);
		if (firedEmployee.isEmpty())
			throw new EmployeeNotFoundException(kimlik);
		return new FireEmployeeResponse("success");
	}

	//@Bulkhead(name = "find",type = Type.SEMAPHORE)
	public EmployeeResponse findEmployee(String identity) {
		TcKimlikNo kimlik = TcKimlikNo.valueOf(identity);
		return hrApplication.getEmployeeInformation(kimlik).map(emp -> modelMapper.map(emp, EmployeeResponse.class))
				.map(emp -> { emp.setStatus("ok"); return emp;})
				.orElseThrow(() -> new EmployeeNotFoundException(kimlik));
	}

	@Transactional
	@Bulkhead(name = "update",type = Type.SEMAPHORE)
	public UpdateEmployeeSalaryResponse updateEmployeeSalary(String identity, UpdateEmployeeSalaryRequest request) {
		TcKimlikNo kimlik = TcKimlikNo.valueOf(identity);
		var employee = hrApplication.updateSalary(kimlik, request.getRate());
		if (employee.isEmpty())
			throw new EmployeeNotFoundException(kimlik);
		return new UpdateEmployeeSalaryResponse("success");
	}

}
