package com.service;

import com.dto.CategoryDTO;
import com.dto.InputCommentDTO;
import com.dto.OutputCommentDTO;
import com.dto.ProductDTO;
import com.entity.CategoryEntity;
import com.entity.CommentEntity;
import com.entity.ProductEntity;
import com.repository.CategoryRepository;
import com.repository.CommentRepository;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Minh Quy on 3/6/2017.
 */
@Service
public class AndroidServices {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CommentRepository commentRepository;

    public List<CategoryDTO> getAllCategory() {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        if (!categoryEntityList.isEmpty()) {
            for (CategoryEntity categoryEntity : categoryEntityList) {
                categoryDTOList.add(convertFromCategoryEntityToCategoryDTO(categoryEntity));
            }
        }
        return categoryDTOList;
    }

    public List<ProductDTO> getProductByCategoryCode(String categoryCode) {
        List<ProductEntity> productEntityList = productRepository.findByCategoryCode(categoryCode);
        List<ProductDTO> productDTOList = new ArrayList<>();
        if (!productEntityList.isEmpty()) {
            for (ProductEntity productEntity : productEntityList) {
                productDTOList.add(convertFromProductEntityToProductDTO(productEntity));
            }
        }
        return productDTOList;
    }

    public ProductDTO likeOrUnlikeProduct(String productCode, Boolean like) {
        ProductEntity productEntity = productRepository.findByProductCode(productCode);
        if (like) {
            productEntity.setNumberOfLover(productEntity.getNumberOfLover() + 1);
        } else {
            productEntity.setNumberOfLover(productEntity.getNumberOfLover() - 1);
        }
        ProductEntity updateProductEntity = productRepository.save(productEntity);
        return convertFromProductEntityToProductDTO(updateProductEntity);
    }

    public OutputCommentDTO createNewCommentForProduct(InputCommentDTO inputCommentDTO) {
        CommentEntity commentEntity = convertFromCommentDTOToCommentEntity(inputCommentDTO);
        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
        return convertFromCommentEntityToCommentDTO(savedCommentEntity);
    }

    public List<OutputCommentDTO> getCommentByProduct(String productCode) {
        List<CommentEntity> commentEntityList = commentRepository.findByProductCode(productCode);
        List<OutputCommentDTO> outputCommentDTOList = new ArrayList<>();
        if (!commentEntityList.isEmpty()) {
            for (CommentEntity commentEntity : commentEntityList) {
                OutputCommentDTO outputCommentDTO = convertFromCommentEntityToCommentDTO(commentEntity);
                outputCommentDTOList.add(outputCommentDTO);
                outputCommentDTO.getDate();
            }
        }
        return outputCommentDTOList;
    }


    private CategoryDTO convertFromCategoryEntityToCategoryDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setCategoryCode(categoryEntity.getCategoryCode());
        categoryDTO.setDetail(categoryEntity.getDetail());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setRecImageUrl(categoryEntity.getRecImageUrl());
        categoryDTO.setSquareImageUrl(categoryEntity.getSquareImageUrl());
        return categoryDTO;
    }

    private ProductDTO convertFromProductEntityToProductDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setDetail(productEntity.getDetail());
        productDTO.setCategoryCode(productEntity.getCategoryCode());
        productDTO.setImageUrl(productEntity.getImageUrl());
        productDTO.setNumberOfLover(productEntity.getNumberOfLover());
        if (productEntity.getNewPrice() != null) {
            productDTO.setNewPrice(productEntity.getNewPrice());
        }
        productDTO.setOldPrice(productEntity.getOldPrice());
        productDTO.setProductCode(productEntity.getProductCode());
        productDTO.setId(productEntity.getId());
        return productDTO;
    }

//    private CommentEntity convertFromCommentDTOToCommentEntity(InputCommentDTO inputCommentDTO) {
//        CommentEntity commentEntity = new CommentEntity();
//        commentEntity.setProductCode(inputCommentDTO.getProductCode());
//        if (inputCommentDTO.getDetail() != null) {
//            commentEntity.setDetail(inputCommentDTO.getDetail());
//        }
//        commentEntity.setDate(inputCommentDTO.getDate());
//        commentEntity.setRate(inputCommentDTO.getRate());
//        commentEntity.setTitle(inputCommentDTO.getTitle());
//        return commentEntity;
//    }

    private CommentEntity convertFromCommentDTOToCommentEntity(InputCommentDTO inputCommentDTO){
        CommentEntity commentEntity = new CommentEntity();
        if (inputCommentDTO.getTitle() != null) {
            commentEntity.setTitle(inputCommentDTO.getTitle());
        }
        if (inputCommentDTO.getDetail() != null) {
            commentEntity.setDetail(inputCommentDTO.getDetail());
        }
        if (inputCommentDTO.getProductCode() != null) {
            commentEntity.setProductCode(inputCommentDTO.getProductCode());
        }
        if (inputCommentDTO.getRate() != null) {
            commentEntity.setRate(Integer.parseInt(inputCommentDTO.getRate()));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (inputCommentDTO.getDate() != null) {
            try {
                Date date = simpleDateFormat.parse(inputCommentDTO.getDate());
                commentEntity.setDate(date);

            } catch (ParseException e) {
                throw new IllegalArgumentException("Date wrong format!");
            }
        }
        return commentEntity;
    }

    private OutputCommentDTO convertFromCommentEntityToCommentDTO(CommentEntity commentEntity) {
        OutputCommentDTO outputCommentDTO = new OutputCommentDTO();
        outputCommentDTO.setId(commentEntity.getId());
        outputCommentDTO.setTitle(commentEntity.getTitle());
        outputCommentDTO.setRate(commentEntity.getRate());
        if (commentEntity.getDetail() != null) {
            outputCommentDTO.setDetail(commentEntity.getDetail());
        }
        outputCommentDTO.setProductCode(commentEntity.getProductCode());
        outputCommentDTO.setDate(commentEntity.getDate());
        return outputCommentDTO;
    }


}
