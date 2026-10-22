package br.com.myfinance.bank;

import br.com.myfinance.category.Category;

import java.util.List;
import java.util.Optional;

public interface BankRepository {
    Bank create(Bank bank);
    Optional<Bank> findById(Integer id);
    boolean findByName(String name);
    boolean findByName(String name, Integer id);
    List<Bank> findAll();
    Bank update(Bank bank);
    Bank softDelete(Integer id);
}
