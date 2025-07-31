package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CommonMappingUtils.class})
public interface FileAssembler {

    @IterableMapping(qualifiedByName = "toUri")
    List<URI> toUriList(List<String> urls);
}
