package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation){
        // Confirm employee exists before creating
        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());
        if (employee == null){
            throw new RuntimeException("Invalid employeeId " + compensation.getEmployee().getEmployeeId());
        }
        // link to existing employee to avoid conflicting records
        compensation.setEmployee(employee);

        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public  Compensation read(String id){
        LOG.debug("Reading compensation with id [{}]", id);

        // Confirm employee exists
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null){
            throw new RuntimeException("Invalid employeeId " + id);
        }

        // Get compensation by employee
        Compensation compensation = compensationRepository.findByEmployee(employee);
        if(compensation == null){
            throw new RuntimeException("Invalid compensation with employeeId " + id);
        }

        return compensation;
    }

}
