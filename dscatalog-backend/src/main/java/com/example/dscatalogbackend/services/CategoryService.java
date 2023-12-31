package com.example.dscatalogbackend.services;

import com.example.dscatalogbackend.dto.CategoryDTO;
import com.example.dscatalogbackend.entities.Category;
import com.example.dscatalogbackend.repositories.CategoryRepository;
import com.example.dscatalogbackend.services.exceptions.DatabaseException;
import com.example.dscatalogbackend.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalObject = repository.findById(id);
        Category categoryEntity = optionalObject.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new CategoryDTO(categoryEntity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category categoryEntity = new Category();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity = repository.save(categoryEntity);
        return new CategoryDTO(categoryEntity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category categoryEntity = repository.getReferenceById(id);
            categoryEntity.setName(categoryDTO.getName());
            categoryEntity = repository.save(categoryEntity);
            return new CategoryDTO(categoryEntity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violation");
        }
    }
}