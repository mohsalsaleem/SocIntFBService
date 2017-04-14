package com.socint.fbpage.service.mapper;

import com.socint.fbpage.domain.*;
import com.socint.fbpage.service.dto.FbPageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FbPage and its DTO FbPageDTO.
 */
@Mapper(componentModel = "spring", uses = {CoverMapper.class, })
public interface FbPageMapper {

    @Mapping(source = "cover.id", target = "coverId")
    FbPageDTO fbPageToFbPageDTO(FbPage fbPage);

    List<FbPageDTO> fbPagesToFbPageDTOs(List<FbPage> fbPages);

    @Mapping(source = "coverId", target = "cover")
    @Mapping(target = "categoryLists", ignore = true)
    FbPage fbPageDTOToFbPage(FbPageDTO fbPageDTO);

    List<FbPage> fbPageDTOsToFbPages(List<FbPageDTO> fbPageDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default FbPage fbPageFromId(Long id) {
        if (id == null) {
            return null;
        }
        FbPage fbPage = new FbPage();
        fbPage.setId(id);
        return fbPage;
    }
    

}
