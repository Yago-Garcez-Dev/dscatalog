package com.example.dscatalogbackend.services;

import com.example.dscatalogbackend.dto.ProductDTO;
import com.example.dscatalogbackend.entities.Product;
import com.example.dscatalogbackend.repositories.ProductRepository;
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
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalObject = repository.findById(id);
        Product productEntity = optionalObject.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new ProductDTO(productEntity, productEntity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product productEntity = new Product();
        productEntity.setName(productDTO.getName());
        productEntity = repository.save(productEntity);
        return new ProductDTO(productEntity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product productEntity = repository.getReferenceById(id);
            productEntity.setName(productDTO.getName());
            productEntity = repository.save(productEntity);
            return new ProductDTO(productEntity);
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