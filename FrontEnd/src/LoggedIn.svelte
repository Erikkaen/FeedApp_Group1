<script>
    import CreatePollComponent from "./components/CreatePollComponent.svelte";
    import VoteComponent from "./components/VoteComponent.svelte";
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();

    export let currentUser;
    let pollRefresh;

    async function logout() {
        try {
            const res = await fetch("http://localhost:8080/users/logout", {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" }
            });

            console.log("logout response status:", res.status);
            if (!res.ok) {
                const text = await res.text();
                console.error("Logout failed:", res.status, text);
                alert("Logout failed: " + text);
                return;
            }

            dispatch("loggedOut");
        } catch (err) {
            console.error("Logout error:", err);
            alert("Logout error: see console.");
        }
    }

    function home() {
        dispatch("homePage");
    }
</script>

<button class="backButton" on:click={logout}>Log out</button>

<main>
    <h2>Welcome, {currentUser.username}!</h2>
    <CreatePollComponent {currentUser} on:pollCreated={() => (pollRefresh = Date.now())} />
    <VoteComponent {currentUser} {pollRefresh} />
</main>

