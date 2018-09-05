package ir.server.boutiqueburger.util;

import ir.server.boutiqueburger.models.Booth;
import ir.server.boutiqueburger.models.Product;
import ir.server.boutiqueburger.models.User;
import ir.server.boutiqueburger.payload.BoothResponse;
import ir.server.boutiqueburger.payload.ProductResponse;
import ir.server.boutiqueburger.payload.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper {

    public static BoothResponse mapBoothToBoothResponse(Booth booth, User creator) {
        BoothResponse boothResponse = new BoothResponse();
        boothResponse.setId(booth.getId());
        boothResponse.setTitle(booth.getTitle());
        boothResponse.setCreationDateTime(booth.getCreatedAt());
       // boothResponse.setExpirationDateTime(booth.getExpirationDateTime());
        //Instant now = Instant.now();
        //boothResponse.setExpired(booth.getExpirationDateTime().isBefore(now));

/*        List<ProductResponse> productResponses = booth.getProducts().stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setTitle(product.getTitle());

            *//*if(choiceVotesMap.containsKey(choice.getId())) {
                productResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                productResponse.setVoteCount(0);
            }*//*
            return productResponse;
        }).collect(Collectors.toList());*/

        //boothResponse.setProducts(productResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        boothResponse.setCreatedBy(creatorSummary);

/*
        if(userVote != null) {
            boothResponse.setSelectedProduct(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);
*/

        return boothResponse;
    }

}
