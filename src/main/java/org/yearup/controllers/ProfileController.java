package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("profiles")
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public Profile createProfile (@RequestBody Profile profile)
    {
        try {

            profileDao.create(profile);

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

        return null;

    }


    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Profile getById (@PathVariable int userId)
    {
        try{

            var profile = profileDao.getById(userId);

            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return profile;

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @PutMapping("{id}")
    @PreAuthorize("permitAll()")
    public Profile updateProfile(@PathVariable int userId, @RequestBody Profile profile)
    {
        try{

            profileDao.updateProfile(userId, profile);

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

        return null;

    }

    @DeleteMapping("{id}")
    @PreAuthorize("permitAll()")
    public void deleteProfile(@PathVariable int userId)
    {
        try {

            var profile = profileDao.getById(userId);

            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            profileDao.deleteProfile(userId);

        }catch(Exception e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }
}
