package org.accounts.accounts.service.impl;

import lombok.AllArgsConstructor;
import org.accounts.accounts.constants.AccountsConstants;
import org.accounts.accounts.dto.AccountsDto;
import org.accounts.accounts.dto.CustomerDto;
import org.accounts.accounts.entity.Account;
import org.accounts.accounts.entity.Customer;
import org.accounts.accounts.exception.CustomerAlreadyExistsException;
import org.accounts.accounts.exception.ResourceNotFoundException;
import org.accounts.accounts.mapper.AccountsMapper;
import org.accounts.accounts.mapper.CustomerMapper;
import org.accounts.accounts.repository.AccountsRepository;
import org.accounts.accounts.repository.CustomerRepository;
import org.accounts.accounts.service.IAccountsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ReadOnlyFileSystemException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number: " + customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 10000000L + new Random().nextLong(90000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);

        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccounts(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean updated = false;
        AccountsDto accountsDto = customerDto.getAccounts();
        if (accountsDto != null) {
            Account account = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "AccountNumber",
                        customerDto.getAccounts().getAccountNumber().toString())
            );
            AccountsMapper.mapToAccount(accountsDto, account);
            account = accountsRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            updated = true;
        }
        return updated;
    }

    @Override
    @Transactional
    public boolean deleteAccount(String mobileNumber) {
        boolean deleted = false;
        CustomerDto customerDto = fetchAccount(mobileNumber);
        if (customerDto != null) {
            Account account = accountsRepository.findById(customerDto.getAccounts().getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber",
                            customerDto.getAccounts().getAccountNumber().toString())
            );
            accountsRepository.delete(account);

            Customer customer = customerRepository.findById(account.getCustomerId()).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", account.getCustomerId().toString())
            );
            customerRepository.delete(customer);
            deleted = true;
        }

        return deleted;
    }

}
