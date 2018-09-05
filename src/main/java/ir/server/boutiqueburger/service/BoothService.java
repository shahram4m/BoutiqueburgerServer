package ir.server.boutiqueburger.service;

import ir.server.boutiqueburger.exception.BadRequestException;
import ir.server.boutiqueburger.exception.ResourceNotFoundException;
import ir.server.boutiqueburger.models.*;
import ir.server.boutiqueburger.payload.PagedResponse;
import ir.server.boutiqueburger.payload.BoothRequest;
import ir.server.boutiqueburger.payload.BoothResponse;
import ir.server.boutiqueburger.repository.BoothRepository;
import ir.server.boutiqueburger.repository.UserRepository;
import ir.server.boutiqueburger.security.UserPrincipal;
import ir.server.boutiqueburger.util.AppConstants;
import ir.server.boutiqueburger.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BoothService {

    @Autowired
    private BoothRepository boothRepository;


    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(BoothService.class);

    public Booth createBooth(BoothRequest boothRequest) {
        Booth booth = new Booth();
        booth.setTitle(boothRequest.getTitle());
        //booth.setExpirationDateTime(boothRequest.getExpirationDateTime());
        logger.info(boothRequest.getTitle());

/*        boothRequest.getProducts().forEach(productRequest -> {
            booth.addProduct(new Product(productRequest.getTitle()));
        });*/

/*        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(boothRequest.getBoothLength().getDays()))
                .plus(Duration.ofHours(boothRequest.getBoothLength().getHours()));*/

        //booth.setExpirationDateTime(expirationDateTime);

        return boothRepository.save(booth);
    }

    public PagedResponse<BoothResponse> getAllBooths(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Booths
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Booth> booths = boothRepository.findAll(pageable);

        if(booths.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), booths.getNumber(),
                    booths.getSize(), booths.getTotalElements(), booths.getTotalPages(), booths.isLast());
        }

        // Map Booths to BoothResponses containing vote counts and booth creator details
        List<Long> boothIds = booths.map(Booth::getId).getContent();
        //Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(boothIds);
        //Map<Long, Long> boothUserVoteMap = getBoothUserVoteMap(currentUser, boothIds);
        Map<Long, User> creatorMap = getBoothCreatorMap(booths.getContent());

        List<BoothResponse> boothResponses = booths.map(booth -> {
            return ModelMapper.mapBoothToBoothResponse(booth,
                    //choiceVoteCountMap,
                    creatorMap.get(booth.getCreatedBy()));
                    //boothUserVoteMap == null ? null : boothUserVoteMap.getOrDefault(booth.getId(), null));
        }).getContent();

        return new PagedResponse<>(boothResponses, booths.getNumber(),
                booths.getSize(), booths.getTotalElements(), booths.getTotalPages(), booths.isLast());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    Map<Long, User> getBoothCreatorMap(List<Booth> booths) {
        // Get Booth Creator details of the given list of booths
        List<Long> creatorIds = booths.stream()
                .map(Booth::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
/*
    public PagedResponse<BoothResponse> getBoothsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all booths created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Booth> booths = boothRepository.findByCreatedBy(user.getId(), pageable);

        if (booths.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), booths.getNumber(),
                    booths.getSize(), booths.getTotalElements(), booths.getTotalPages(), booths.isLast());
        }

        // Map Booths to BoothResponses containing vote counts and booth creator details
        List<Long> boothIds = booths.map(Booth::getId).getContent();
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(boothIds);
        Map<Long, Long> boothUserVoteMap = getBoothUserVoteMap(currentUser, boothIds);

        List<BoothResponse> boothResponses = booths.map(booth -> {
            return ModelMapper.mapBoothToBoothResponse(booth,
                    choiceVoteCountMap,
                    user,
                    boothUserVoteMap == null ? null : boothUserVoteMap.getOrDefault(booth.getId(), null));
        }).getContent();

        return new PagedResponse<>(boothResponses, booths.getNumber(),
                booths.getSize(), booths.getTotalElements(), booths.getTotalPages(), booths.isLast());
    }

    public PagedResponse<BoothResponse> getBoothsVotedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all boothIds in which the given username has voted
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        //Page<Long> userVotedBoothIds = voteRepository.findVotedBoothIdsByUserId(user.getId(), pageable);

*//*
        if (userVotedBoothIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userVotedBoothIds.getNumber(),
                    userVotedBoothIds.getSize(), userVotedBoothIds.getTotalElements(),
                    userVotedBoothIds.getTotalPages(), userVotedBoothIds.isLast());
        }
*//*

        // Retrieve all booth details from the voted boothIds.
       // List<Long> boothIds = userVotedBoothIds.getContent();

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Booth> booths = boothRepository.findByIdIn(boothIds, sort);

        // Map Booths to BoothResponses containing vote counts and booth creator details
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(boothIds);
        Map<Long, Long> boothUserVoteMap = getBoothUserVoteMap(currentUser, boothIds);
        Map<Long, User> creatorMap = getBoothCreatorMap(booths);

        List<BoothResponse> boothResponses = booths.stream().map(booth -> {
            return ModelMapper.mapBoothToBoothResponse(booth,
                    choiceVoteCountMap,
                    creatorMap.get(booth.getCreatedBy()),
                    boothUserVoteMap == null ? null : boothUserVoteMap.getOrDefault(booth.getId(), null));
        }).collect(Collectors.toList());

        return new PagedResponse<>(boothResponses, userVotedBoothIds.getNumber(), userVotedBoothIds.getSize(), userVotedBoothIds.getTotalElements(), userVotedBoothIds.getTotalPages(), userVotedBoothIds.isLast());
    }

    public BoothResponse getBoothById(Long boothId, UserPrincipal currentUser) {
        Booth booth = boothRepository.findById(boothId).orElseThrow(
                () -> new ResourceNotFoundException("Booth", "id", boothId));

        // Retrieve Vote Counts of every choice belonging to the current booth
        List<ChoiceVoteCount> votes = voteRepository.countByBoothIdGroupByChoiceId(boothId);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        // Retrieve booth creator details
        User creator = userRepository.findById(booth.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", booth.getCreatedBy()));

        // Retrieve vote done by logged in user
        Vote userVote = null;
        if(currentUser != null) {
            userVote = voteRepository.findByUserIdAndBoothId(currentUser.getId(), boothId);
        }

        return ModelMapper.mapBoothToBoothResponse(booth, choiceVotesMap,
                creator, userVote != null ? userVote.getChoice().getId(): null);
    }

    public BoothResponse castVoteAndGetUpdatedBooth(Long boothId, VoteRequest voteRequest, UserPrincipal currentUser) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new ResourceNotFoundException("Booth", "id", boothId));

        if(booth.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This Booth has already expired");
        }

        User user = userRepository.getOne(currentUser.getId());

        Choice selectedChoice = booth.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

        Vote vote = new Vote();
        vote.setBooth(booth);
        vote.setUser(user);
        vote.setChoice(selectedChoice);

        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Booth {}", currentUser.getId(), boothId);
            throw new BadRequestException("Sorry! You have already cast your vote in this booth");
        }

        //-- Vote Saved, Return the updated Booth Response now --

        // Retrieve Vote Counts of every choice belonging to the current booth
        List<ChoiceVoteCount> votes = voteRepository.countByBoothIdGroupByChoiceId(boothId);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        // Retrieve booth creator details
        User creator = userRepository.findById(booth.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", booth.getCreatedBy()));

        return ModelMapper.mapBoothToBoothResponse(booth, choiceVotesMap, creator, vote.getChoice().getId());
    }



    private Map<Long, Long> getChoiceVoteCountMap(List<Long> boothIds) {
        // Retrieve Vote Counts of every Choice belonging to the given boothIds
        List<ChoiceVoteCount> votes = voteRepository.countByBoothIdInGroupByChoiceId(boothIds);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        return choiceVotesMap;
    }

    private Map<Long, Long> getBoothUserVoteMap(UserPrincipal currentUser, List<Long> boothIds) {
        // Retrieve Votes done by the logged in user to the given boothIds
        Map<Long, Long> boothUserVoteMap = null;
        if(currentUser != null) {
            List<Vote> userVotes = voteRepository.findByUserIdAndBoothIdIn(currentUser.getId(), boothIds);

            boothUserVoteMap = userVotes.stream()
                    .collect(Collectors.toMap(vote -> vote.getBooth().getId(), vote -> vote.getChoice().getId()));
        }
        return boothUserVoteMap;
    }

    */
}