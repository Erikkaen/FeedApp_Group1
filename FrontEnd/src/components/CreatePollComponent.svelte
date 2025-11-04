<script>
  let question = "";
  let options = [""];

  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  async function createPoll() {
    const pollData = {
      question,
      publishedAt: new Date().toISOString(),
      validUntil: null,
      options: options.map((opt, i) => ({
        caption: opt,
        presentationOrder: i + 1
      }))
    };

    const res = await fetch("http://localhost:8080/polls", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(pollData),
    });

    const pollId = await res.text();
    console.log("Created poll:", pollId);
    dispatch("pollCreated");
  }

  function addOption() {
    options = [...options, ""];
  }
</script>

<div class="component">
  <h2>Create Poll</h2>
  <input placeholder="Poll Question" bind:value={question} />

  {#each options as opt, i}
    <input placeholder={`Option ${i+1}`} bind:value={options[i]} />
  {/each}

  <button on:click={addOption}>+ Add Option</button>
  <button on:click={createPoll}>Create Poll</button>
</div>

<style>
  .component {
    border: 1px solid #ccc;
    padding: 20px;
    margin-bottom: 30px;
    border-radius: 5px;
  }
  input {
    display: block;
    margin-bottom: 10px;
    padding: 5px;
    width: 100%;
  }
  button {
    background: lightgrey;
    border: none;
    padding: 6px 12px;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
  }
  button:hover {
    background: #ddd;
  }
</style>
