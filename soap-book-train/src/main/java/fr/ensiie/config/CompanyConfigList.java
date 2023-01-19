package fr.ensiie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CompanyConfigList {

    @Value("${providers.train.sncf}")
    private String sncfDomainName;

    @Value("${providers.train.la-renfe}")
    private String laRenfeDomainName;

    @Bean(name = "companyConfigurations")
    public List<CompanyConfig> companyConfigurations() {
        List<CompanyConfig> companyConfigList = new ArrayList<>();
        companyConfigList.add(new CompanyConfig(1, "SNCF", sncfDomainName));
        companyConfigList.add(new CompanyConfig(2, "LA RENFE", laRenfeDomainName));
        return companyConfigList;
    }

}
