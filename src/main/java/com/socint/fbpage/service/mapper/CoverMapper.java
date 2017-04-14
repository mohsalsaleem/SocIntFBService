package com.socint.fbpage.service.mapper;

import com.socint.fbpage.domain.*;
import com.socint.fbpage.service.dto.CoverDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Cover and its DTO CoverDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CoverMapper {

    CoverDTO coverToCoverDTO(Cover cover);

    List<CoverDTO> coversToCoverDTOs(List<Cover> covers);

    Cover coverDTOToCover(CoverDTO coverDTO);

    List<Cover> coverDTOsToCovers(List<CoverDTO> coverDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Cover coverFromId(Long id) {
        if (id == null) {
            return null;
        }
        Cover cover = new Cover();
        cover.setId(id);
        return cover;
    }
    

}
