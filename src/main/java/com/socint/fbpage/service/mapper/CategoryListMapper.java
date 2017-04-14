package com.socint.fbpage.service.mapper;

import com.socint.fbpage.domain.*;
import com.socint.fbpage.service.dto.CategoryListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CategoryList and its DTO CategoryListDTO.
 */
@Mapper(componentModel = "spring", uses = {FbPageMapper.class, })
public interface CategoryListMapper {

    @Mapping(source = "fbPage.id", target = "fbPageId")
    CategoryListDTO categoryListToCategoryListDTO(CategoryList categoryList);

    List<CategoryListDTO> categoryListsToCategoryListDTOs(List<CategoryList> categoryLists);

    @Mapping(source = "fbPageId", target = "fbPage")
    CategoryList categoryListDTOToCategoryList(CategoryListDTO categoryListDTO);

    List<CategoryList> categoryListDTOsToCategoryLists(List<CategoryListDTO> categoryListDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default CategoryList categoryListFromId(Long id) {
        if (id == null) {
            return null;
        }
        CategoryList categoryList = new CategoryList();
        categoryList.setId(id);
        return categoryList;
    }
    

}
