package com.grayzone.global.job;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompanyLegalDistrictMappingJobConfig {
  private static final int CHUNK_SIZE = 5000;
  private final CompanyRepository companyRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final DataSource dataSource;

  private Map<String, LegalDistrict> legalDistrictCache;

  @PostConstruct
  public void loadLegalDistrictCache() {
    legalDistrictCache = legalDistrictRepository.findAll()
      .stream()
      .collect(Collectors.toMap(LegalDistrict::getAddress, ld -> ld));
  }

  @Bean
  public Job companyLegalDistrictJob(JobRepository jobRepository, Step companyLegalDistrictStep) {
    return new JobBuilder("companyLegalDistrictJob", jobRepository)
      .start(companyLegalDistrictStep)
      .build();
  }

  @Bean
  public Step companyLegalDistrictStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("companyLegalDistrictStep", jobRepository)
      .<Company, CompanyUpdateDto>chunk(CHUNK_SIZE, transactionManager)
      .reader(companyItemReader())
      .processor(companyItemProcessor())
      .writer(companyItemWriter())
      .build();
  }

  public ItemReader<Company> companyItemReader() {
    return new ItemReader<>() {
      private Long lastId = 0L;
      private List<Company> currentBatch = List.of();
      private int currentIndex = 0;

      @Override
      public Company read() {
        if (currentIndex >= currentBatch.size()) {
          currentBatch = companyRepository.findByLegalDistrictIdIsNullAndIdGreaterThanOrderByIdAsc(
            lastId,
            PageRequest.of(0, CHUNK_SIZE)
          );

          if (currentBatch.isEmpty()) {
            return null;
          }

          currentIndex = 0;
        }

        Company nextCompany = currentBatch.get(currentIndex);
        currentIndex++;

        lastId = nextCompany.getId();

        return nextCompany;
      }
    };
  }

  @Bean
  public ItemProcessor<Company, CompanyUpdateDto> companyItemProcessor() {
    return company -> {
      if (company.getSiteFullAddress() == null) return null;

      String[] parts = company.getSiteFullAddress().split(" ");
      if (parts.length < 3) return null;

      String keyAddress = String.join(" ", parts[0], parts[1], parts[2]);

      LegalDistrict legalDistrict = legalDistrictCache.get(keyAddress);

      if (legalDistrict == null) {
        return null;
      }

      return new CompanyUpdateDto(company.getId(), legalDistrict.getId());
    };
  }

  @Bean
  public JdbcBatchItemWriter<CompanyUpdateDto> companyItemWriter() {
    return new JdbcBatchItemWriterBuilder<CompanyUpdateDto>()
      .dataSource(dataSource)
      .sql("UPDATE companies SET legal_district_id = :legalDistrictId WHERE id = :id")
      .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
      .build();
  }
}
