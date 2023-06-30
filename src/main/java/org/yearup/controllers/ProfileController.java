package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Profile createProfile(@RequestBody Profile profile)
    {
        try {

            profileDao.create(profile);
            return profile;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @GetMapping("users/{id}")
    public Profile getById(Principal principal, @PathVariable int id)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            if (userId != id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
            }

            var profile = profileDao.getById(id);

            if (profile == null) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            }

            return profile;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @GetMapping("admins/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Profile getByIdAdmin(@PathVariable int id)
    {
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

    @PutMapping("users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Profile updateProfile(Principal principal, @PathVariable int id, @RequestBody Profile profile)
    {
        try{

            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            if (userId != id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
            }

            Profile existingProfile = profileDao.getById(id);
            if (existingProfile == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found.");
            }

            profileDao.updateProfile(id, profile);

            return profile;

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @PutMapping("admins/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.OK)
    public Profile updateProfileAdmin(@PathVariable int id, @RequestBody Profile profile)
    {
        try{

            Profile existingProfile = profileDao.getById(id);
            if (existingProfile == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found.");
            }

            profileDao.updateProfile(id, profile);

            return profile;

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @DeleteMapping("users/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProfile(Principal principal, @PathVariable int id)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            if (userId != id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
            }

            var profile = profileDao.getById(id);

            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            profileDao.deleteProfile(id);

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @DeleteMapping("admins/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProfileAdmin(@PathVariable int id)
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
