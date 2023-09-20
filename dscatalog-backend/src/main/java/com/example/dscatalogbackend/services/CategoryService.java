package com.example.dscatalogbackend.services;

import com.example.dscatalogbackend.dto.CategoryDTO;
import com.example.dscatalogbackend.entities.Category;
import com.example.dscatalogbackend.repositories.CategoryRepository;
import com.example.dscatalogbackend.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalObject = repository.findById(id);
        Category categoryEntity = optionalObject.orElseThrow(() -> new EntityNotFoundException("Entity not found."));
        return new CategoryDTO(categoryEntity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category categoryEntity = new Category();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity = repository.save(categoryEntity);
        return new CategoryDTO(categoryEntity);
    }
}