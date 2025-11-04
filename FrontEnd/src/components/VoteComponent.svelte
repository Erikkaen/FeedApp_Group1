<script>
  export let currentUser;
  export let pollRefresh;

  let polls = {};
  let selectedOptions = {};
  let voting = {};

  async function fetchPolls() {
    try {
      const res = await fetch("http://localhost:8080/polls");
      if (!res.ok) throw new Error("Failed to fetch polls");
      polls = await res.json();
      console.log("Fetched polls:", polls);
    } catch (err) {
      console.error("Error fetching polls:", err);
    }
  }

  import { onMount } from "svelte";
  onMount(fetchPolls);

  $: if (pollRefresh !== undefined) {
    fetchPolls();
  }

  async function submitVote(pollId) {
    const option = selectedOptions[pollId];
    if (!option) return;
    voting[pollId] = true;
    const voteData = {
      publishedAt: new Date().toISOString(),
      option
    };
    try {
      const res = await fetch(
        `http://localhost:8080/votes/${pollId}/${currentUser.username}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(voteData),
        }
      );
      if (!res.ok) throw new Error("Failed to submit vote");
      console.log("Voted:", option.caption, "on poll", pollId);
      await fetchPolls();
    } catch (err) {
      console.error("Error voting:", err);
    } finally {
      voting[pollId] = false;
    }
  }
</script>

<div class="component">
  <h2>Vote on Polls</h2>

  {#if Object.keys(polls).length === 0}
    <p>No polls available yet.</p>
  {:else}
    {#each Object.entries(polls) as [pollId, poll]}
      <div class="poll">
        <h4>Poll #{pollId}</h4>
        <h3>{poll.question}</h3>
        <form on:submit|preventDefault={() => submitVote(pollId)}>
          <ul>
            {#each poll.options as opt}
              <li>
                <label>
                  <input
                    type="radio"
                    name={`option-${pollId}`}
                    bind:group={selectedOptions[pollId]}
                    value={opt}
                    disabled={voting[pollId]}
                  />
                  {opt.presentationOrder}. {opt.caption}
                  {#if opt.voteCount !== undefined}
                    <span style="margin-left:10px; color: #555;">Votes: {opt.voteCount}</span>
                  {/if}
                </label>
              </li>
            {/each}
          </ul>
          <button type="submit" disabled={voting[pollId] || !selectedOptions[pollId]}>Vote</button>
        </form>
      </div>
    {/each}
  {/if}
</div>

<style>
  .component {
    border: 1px solid #ccc;
    padding: 20px;
    margin-bottom: 30px;
    border-radius: 5px;
  }
  .poll {
    margin-bottom: 20px;
    padding: 10px;
    border-bottom: 1px solid #eee;
  }
  ul {
    list-style: none;
    padding-left: 0;
  }
  li {
    margin-bottom: 8px;
    display: flex;
    align-items: center;
    gap: 10px;
  }
</style>
