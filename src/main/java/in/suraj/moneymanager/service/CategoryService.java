package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.CategoryDto;
import in.suraj.moneymanager.entity.Category;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public CategoryDto saveCategory(CategoryDto dto){
        Profile currentProfile = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(dto.getName(), currentProfile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
        }

        Category newCategory = toEntity(dto, currentProfile);
        newCategory = categoryRepository.save(newCategory);
        return toDto(newCategory);
    }

    public List<CategoryDto> getCategoriesForCurrentUser(){
        Long profileId = profileService.getCurrentProfile().getId();
        List<Category> categories = categoryRepository.findByProfileId(profileId);
        return categories.stream().map(this::toDto).toList();
    }

    private Category toEntity(CategoryDto dto, Profile profile){
        return Category.builder()
                .name(dto.getName())
                .profile(profile)
                .type(dto.getType())
                .icon(dto.getIcon())
                .build();
    }

    private CategoryDto toDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .profileId(category.getProfile() != null ? category.getProfile().getId() : null)
                .name(category.getName())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .type(category.getType())
                .build();
    }
}
