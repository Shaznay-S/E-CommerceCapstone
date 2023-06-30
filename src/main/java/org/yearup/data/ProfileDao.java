package org.yearup.data;


import org.yearup.models.Profile;

import java.util.Optional;

public interface ProfileDao
{
    Profile create(Profile profile);
    Profile getById(int userId);
    Profile updateProfile(int userId, Profile profile);
    void deleteProfile(int userId);


}
