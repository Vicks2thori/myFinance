package br.com.myfinance.bank;

import br.com.myfinance.category.Category;
import br.com.myfinance.category.CategoryDTO;
import br.com.myfinance.category.CategoryRepository;
import br.com.myfinance.exeption.DuplicateResourceException;
import br.com.myfinance.exeption.ResourceNotFoundException;
import java.util.List;

public class BankService {

    private final BankRepository repository;

    public BankService(BankRepository repository) {
        this.repository = repository;
    }

    public BankDTO.Response create(BankDTO.Create dto) {

        if (repository.findByName(dto.name())) {
            throw new DuplicateResourceException("Já existe um banco cadastrado com o nome: " + dto.name());
        }

        Bank bank = new Bank(
                null,
                dto.name(),
                dto.description(),
                dto.active(),
                null,
                null,
                null
        );

        return convert(repository.create(bank));
    }

    public BankDTO.Response findById(Integer id) {

        Bank bank = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Banco não encontrado com o id: " + id)
                );

        return convert(bank);
    }

    public List<BankDTO.Response> findAll() {
        return repository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    public BankDTO.Response update(BankDTO.Update dto) {

        Bank exist = repository.findById(dto.id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Banco não encontrado com o id: " + dto.id())
                );

        if (dto.name() != null) {
            if (repository.findByName(dto.name(), dto.id())) {
                throw new DuplicateResourceException("Já existe um bancp cadastrado com o nome: " + dto.name());
            }

            exist.setName(dto.name());
        }

        if (dto.description() != null) {
            exist.setDescription(dto.description());
        }

        if (dto.active() != null) {
            exist.setActive(dto.active());
        }

        Bank updated = repository.update(exist);
        return convert(updated);
    }

    public BankDTO.Response delete(BankDTO.Delete dto) {
        repository.findById(dto.id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Banco não encontrado com o id: " + dto.id())
                );

        return convert(repository.softDelete(dto.id()));
    }

    private BankDTO.Response convert(Bank bank) {
        return new BankDTO.Response(
                bank.getId(),
                bank.getName(),
                bank.getDescription(),
                bank.isActive(),
                bank.getCreatedAt(),
                bank.getUpdatedAt(),
                bank.getDeletedAt()
        );
    }
}