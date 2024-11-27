package com.web.serviceorientedweb.config;

import com.web.serviceorientedweb.models.Race;
import org.web.transportapi.dto.RaceViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurator {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.createTypeMap(Race.class, RaceViewDto.class)
                .addMapping(src -> src.getTransport().getModel(), RaceViewDto::setModel);
     return modelMapper;
    }
}