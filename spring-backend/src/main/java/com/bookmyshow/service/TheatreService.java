package com.bookmyshow.service;

import com.bookmyshow.domain.Theatre;
import com.bookmyshow.domain.User;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.TheatreResponse;
import com.bookmyshow.repository.TheatreRepository;
import com.bookmyshow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ApiResponse<List<TheatreResponse>> getAll() {
        List<TheatreResponse> data = theatreRepository.findAll().stream()
                .map(TheatreResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("All theatres fetched", data);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<TheatreResponse>> getByOwnerId(Long ownerId) {
        List<TheatreResponse> data = theatreRepository.findByOwner_Id(ownerId).stream()
                .map(TheatreResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("All theatres fetched", data);
    }

    @Transactional
    public ApiResponse<TheatreResponse> add(Theatre theatre) {
        if (theatre.getOwnerId() != null) {
            userRepository.findById(theatre.getOwnerId()).ifPresent(theatre::setOwner);
        }
        theatre = theatreRepository.save(theatre);
        return ApiResponse.success("New theatre has been added", TheatreResponse.from(theatre));
    }

    @Transactional
    public ApiResponse<TheatreResponse> update(Long id, Theatre update) {
        return theatreRepository.findById(id)
                .map(t -> {
                    if (update.getName() != null) t.setName(update.getName());
                    if (update.getAddress() != null) t.setAddress(update.getAddress());
                    if (update.getPhone() != null) t.setPhone(update.getPhone());
                    if (update.getEmail() != null) t.setEmail(update.getEmail());
                    if (update.getIsActive() != null) t.setIsActive(update.getIsActive());
                    if (update.getOwnerId() != null) {
                        userRepository.findById(update.getOwnerId()).ifPresent(t::setOwner);
                    }
                    return ApiResponse.success("Theatre has been updated", TheatreResponse.from(theatreRepository.save(t)));
                })
                .orElse(ApiResponse.failure("Theatre not found"));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        if (!theatreRepository.existsById(id)) {
            return ApiResponse.failure("Theatre not found");
        }
        theatreRepository.deleteById(id);
        return ApiResponse.success("Theatre has been deleted");
    }
}
