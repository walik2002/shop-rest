package com.belous.api.services;

import com.belous.api.models.RoleEntity;
import com.belous.api.repos.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        log.info("Saving role {} to the database", roleEntity.getName());
        return roleJpaRepository.save(roleEntity);
    }


}