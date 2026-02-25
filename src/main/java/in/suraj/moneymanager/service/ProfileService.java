package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.ProfileDto;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static in.suraj.moneymanager.constants.EmailConstants.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;

    public ProfileDto registerProfile(ProfileDto profileDto){
        Profile newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        //send activation email
        String activationLink = ACCOUNT_ACTIVATION_LINK + newProfile.getActivationToken();
        String body = ACCOUNT_ACTIVATION_BODY + activationLink;
        emailService.sendEmail(newProfile.getEmail(), ACCOUNT_ACTIVATION_SUBJECT, body);
        return toDto(newProfile);
    }

    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken).map(
                profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                }
        ).orElse(false);
    }

    private Profile toEntity(ProfileDto profileDto){
        return Profile.builder()
                .id(profileDto.getId())
                .name(profileDto.getName())
                .email(profileDto.getEmail())
                .password(profileDto.getPassword())
                .imageUrl(profileDto.getImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .modifiedAt(profileDto.getModifiedAt())
                .build();
    }

    private ProfileDto toDto(Profile profile){
        return ProfileDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .email(profile.getEmail())
                .imageUrl(profile.getImageUrl())
                .createdAt(profile.getCreatedAt())
                .modifiedAt(profile.getModifiedAt())
                .build();
    }

}
