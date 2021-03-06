package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;

public class FollowingServiceProxyTest {

    private FollowingRequest validRequest;
    private FollowingRequest invalidRequest;

    private FollowingResponse successResponse;
    private FollowingResponse failureResponse;

    private ServerFacade mockServerFacade;
    private FollowingServiceProxy followingServiceProxy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null, "password");

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png", "password");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png", "password");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png", "password");

        // Setup request objects to use in the tests
        validRequest = new FollowingRequest(currentUser, 3, null);
        invalidRequest = new FollowingRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowees(validRequest, "/following")).thenReturn(successResponse);

        failureResponse = new FollowingResponse("An exception occured");
        Mockito.when(mockServerFacade.getFollowees(invalidRequest, "/following")).thenReturn(failureResponse);

        // Create a CountServiceProxy instance and wrap it with a spy that will use the mock edu.byu.cs.tweeter.server.service
        followingServiceProxy = Mockito.spy(new FollowingServiceProxy());
        Mockito.when(followingServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowees_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowingResponse response = followingServiceProxy.getFollowees(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowees_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FollowingResponse response = followingServiceProxy.getFollowees(validRequest);

        for(User user : response.getFollowees()) {
            Assertions.assertNotNull(user.getImageBytes());
        }
    }

    @Test
    public void testGetFollowees_invalidRequest_returnsNoFollowees() throws IOException, TweeterRemoteException {
        FollowingResponse response = followingServiceProxy.getFollowees(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
