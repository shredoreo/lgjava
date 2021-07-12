package com.shred.spring.factory;

import com.shred.spring.pojo.Company;
import org.springframework.beans.factory.FactoryBean;

public class CompanyFactoryBean implements FactoryBean<Company> {

    private String companyInfo;//公司名称、地址、规模

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Company getObject() throws Exception {
        Company company = new Company();
        String[] split = companyInfo.split(",");
        company.setName(split[0]);
        company.setAddress(split[1]);
        company.setScale(split[2]);

        return company;
    }

    @Override
    public Class<?> getObjectType() {
        return Company.class;
    }
}
