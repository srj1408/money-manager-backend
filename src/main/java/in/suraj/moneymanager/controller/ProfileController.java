package in.suraj.moneymanager.controller;

import in.suraj.moneymanager.dto.ProfileDto;
import in.suraj.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static in.suraj.moneymanager.constants.UrlConstants.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(REGISTER)
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto){
        ProfileDto registeredProfile = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping(ACTIVATE)
    public ResponseEntity<String> activateProfile(@RequestParam String token){
        return profileService.activateProfile(token)
                ? ResponseEntity.ok("Profile activated successfully!")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
    }
}
