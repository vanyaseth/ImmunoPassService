package com.immunopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.immunopass.controller.ImmunopassController;
import com.immunopass.entity.ImmunopassEntity;
import com.immunopass.model.Immunopass;
import com.immunopass.model.VerifyImmunopassRequest;
import com.immunopass.repository.ImmunopassRepository;
import com.immunopass.restclient.SMSService;


@Service
public class ImmunopassService implements ImmunopassController {

    @Autowired
    private ImmunopassRepository immunopassRepository;

    @Autowired
    private SMSService smsService;

    @Override public Immunopass createImmunopass(final Immunopass immunopass) {
        String code = null;
        while (checkCodeForUniqueness(code) == false) {
            code = smsService.generateUniqueCode(8);
        }

        ImmunopassEntity immunopassEntity =
                ImmunopassEntity.builder()
                        .userName(immunopass.getUserName())
                        .userMobile(immunopass.getUserMobile())
                        .userEmpId(immunopass.getUserMobile())
                        .userGovernmentId(immunopass.getUserGovernmentId())
                        .userLocation(immunopass.getUserLocation())
                        .immunopassCode(code)
                        .immunoTestResult(immunopass.getImmunoTestResult())
                        .build();
        immunopassEntity = immunopassRepository.save(immunopassEntity);
        smsService.sendImmunoPass(immunopassEntity.getUserMobile(), immunopassEntity.getImmunopassCode(),
                immunopassEntity.getImmunoTestResult().toString());
        return mapEntityToModel(immunopassEntity);
    }

    @Override public Immunopass verifyImmunopass(final VerifyImmunopassRequest immunopass) {
        if (immunopass.getImmunopassCode() != null) {
            return immunopassRepository
                    .findByImmunopassCode(immunopass.getImmunopassCode())
                    .map(this::mapEntityToModel)
                    .orElse(null);
        } else if (immunopass.getUserMobile() != null) {
            return immunopassRepository
                    .findByUserMobile(immunopass.getUserMobile())
                    .map(this::mapEntityToModel)
                    .orElse(null);
        } else {
            return null;
        }
    }

    private Immunopass mapEntityToModel(ImmunopassEntity immunopassEntity) {
        return Immunopass.builder()
                .id(immunopassEntity.getId())
                .userName(immunopassEntity.getUserName())
                .userMobile(immunopassEntity.getUserMobile())
                .userEmpId(immunopassEntity.getUserEmpId())
                .userGovernmentId(immunopassEntity.getUserGovernmentId())
                .userLocation(immunopassEntity.getUserLocation())
                .immunopassCode(immunopassEntity.getImmunopassCode())
                .immunoTestResult(immunopassEntity.getImmunoTestResult())
                .build();
    }

    private boolean checkCodeForUniqueness(String code) {
        if (code == null || immunopassRepository.findByImmunopassCode(code).isPresent()) {
            return false;
        }
        return true;
    }

}
