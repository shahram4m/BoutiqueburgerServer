package ir.server.boutiqueburger.controller;

import ir.server.boutiqueburger.models.Booth;
import ir.server.boutiqueburger.payload.ApiResponse;
import ir.server.boutiqueburger.payload.BoothRequest;
import ir.server.boutiqueburger.payload.BoothResponse;
import ir.server.boutiqueburger.payload.PagedResponse;
import ir.server.boutiqueburger.repository.BoothRepository;
import ir.server.boutiqueburger.repository.UserRepository;
import ir.server.boutiqueburger.security.CurrentUser;
import ir.server.boutiqueburger.security.UserPrincipal;
import ir.server.boutiqueburger.service.BoothService;
import ir.server.boutiqueburger.util.AppConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/booths")
public class BoothController {

    @Autowired
    private BoothRepository boothRepository;

/*
    @Autowired
    private VoteRepository voteRepository;
*/

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoothService boothService;

    private static final Logger logger = LoggerFactory.getLogger(BoothController.class);

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createBooth(@Valid @RequestBody BoothRequest boothRequest) {
        Booth booth = boothService.createBooth(boothRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{boothId}")
                .buildAndExpand(booth.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "booth Created Successfully"));
    }



    @GetMapping("/getall")
    public PagedResponse<BoothResponse> getBooth(@CurrentUser UserPrincipal currentUser,
                                                 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return boothService.getAllBooths(currentUser, page, size);
    }

/*
    @GetMapping("/{boothId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId) {
        return boothService.getPollById(pollId, currentUser);
    }

    @PostMapping("/{boothId}/votes")
    @PreAuthorize("hasRole('USER')")
    public BoothResponse castVote(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long pollId,
                                 @Valid @RequestBody VoteRequest voteRequest) {
        return pollService.castVoteAndGetUpdatedPoll(boothId, voteRequest, currentUser);
    }
*/

}