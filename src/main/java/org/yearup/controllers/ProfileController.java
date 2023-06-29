package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profiles")
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Profile createProfile(@RequestBody Profile profile) {
        try {

            profileDao.create(profile);
            return profile;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.id")
    public Profile getById(@PathVariable int id, Authentication authentication) {
        try {

            var profile = profileDao.getById(id);

            if (profile == null) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            }

            return profile;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

//    @GetMapping("{id}")
//    public Profile getById(@PathVariable int id, Authentication authentication) {
//        User authenticatedUser = userDao.getUserById(id);
//
//        if (authenticatedUser != null) {
//            boolean isAdmin = authenticatedUser.getAuthorities().stream()
//                    .anyMatch(authority -> authority.getName().equals("ROLE_ADMIN"));
//
//            if (isAdmin || authenticatedUser.getId() == id) {
//                Profile profile = profileDao.getById(id);
//
//                if (profile == null) {
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//                }
//                return profile;
//            } else {
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this profile");
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in to access a profile");
//        }
//    }




    @PutMapping("{id}")
    @PreAuthorize("permitAll()")
    public Profile updateProfile(@PathVariable int id, @RequestBody Profile profile)
    {
        try{

            profileDao.updateProfile(id, profile);

            return profile;

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @DeleteMapping("{id}")
    @PreAuthorize("permitAll()")
    public void deleteProfile(@PathVariable int id)
    {
        try {

            var profile = profileDao.getById(id);

            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            profileDao.deleteProfile(id);

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

}
