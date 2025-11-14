<script>
    import CreateUserComponent from "./components/CreateUserComponent.svelte";
    import VoteComponent from "./components/VoteComponent.svelte";
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();

    let currentUser = { username: "guest" };
    let pollRefresh;

    function register() {
        dispatch("register");
    }

    function home() {
        dispatch("homePage");
    }
</script>

<main>
    <div>
        <!-- Go to registration -->
        <div class="homeComponent">
            <h3>Don't have an account?</h3>
            <button class="homeButton" on:click={register}>Register account</button>
        </div>
        <div>
            <button class="backButton" on:click={home}>Go back</button>
            <h2>Welcome, Guest</h2>
            <VoteComponent {currentUser} {pollRefresh} />
        </div>
    </div>
</main>


