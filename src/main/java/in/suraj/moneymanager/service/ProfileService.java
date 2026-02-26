package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.AuthDto;
import in.suraj.moneymanager.dto.ProfileDto;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.repository.ProfileRepository;
import in.suraj.moneymanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static in.suraj.moneymanager.constants.EmailConstants.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
                .password(passwordEncoder.encode(profileDto.getPassword()))
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

    public boolean isProfileActive(String email){
        return profileRepository.findByEmail(email).map(Profile::getIsActive).orElse(false);
    }

    public Profile getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            String email = authentication.getName();
            return profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: "+email));
        }
        return null;
    }

    public ProfileDto getPublicProfile(String email){
        Profile currentUser;
        if(email == null) currentUser = getCurrentProfile();
        else currentUser = profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: "+email));
        return currentUser != null ? toDto(currentUser): null;
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            String token = jwtUtil.generateToken(authDto.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authDto.getEmail())
                    );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
