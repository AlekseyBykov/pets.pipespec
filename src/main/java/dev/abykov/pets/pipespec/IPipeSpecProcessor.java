package dev.abykov.pets.pipespec;

import dev.abykov.pets.pipespec.models.pipespec.PipeSpecFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;

public interface IPipeSpecProcessor {

    PipeSpecFile process(MultipartFile file, String documentMarker, String schemeName);

    String toPlainText(PipeSpecFile pipespec);

    byte[] toByteArray(PipeSpecFile pipespec, Charset charset);
}
