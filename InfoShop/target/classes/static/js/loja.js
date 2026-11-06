(() => {
    const lista = document.getElementById("lista");
    if (!lista) return;

    const cards = Array.from(lista.querySelectorAll(".produto"));
    const busca = document.getElementById("busca");
    const formBusca = document.getElementById("form-busca");
    const precoMin = document.getElementById("preco-min");
    const precoMax = document.getElementById("preco-max");
    const limpar = document.getElementById("limpar");
    const vazio = document.getElementById("vazio");
    const contagem = document.getElementById("contagem");
    const ordens = document.querySelectorAll('input[name="ordem"]');

    // Remove acentos e maiúsculas para a busca achar "camera" em "Câmera"
    const normalizar = (texto) =>
        texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();

    const comparadores = {
        padrao: (a, b) => Number(a.dataset.indice) - Number(b.dataset.indice),
        menor: (a, b) => parseFloat(a.dataset.valor) - parseFloat(b.dataset.valor),
        maior: (a, b) => parseFloat(b.dataset.valor) - parseFloat(a.dataset.valor),
        nome: (a, b) => a.dataset.nome.localeCompare(b.dataset.nome, "pt-BR"),
    };

    function atualizar() {
        const termo = normalizar(busca.value.trim());
        const min = parseFloat(precoMin.value);
        const max = parseFloat(precoMax.value);
        const ordem = document.querySelector('input[name="ordem"]:checked').value;

        let visiveis = 0;
        cards.forEach((card) => {
            const valor = parseFloat(card.dataset.valor);
            const passa =
                normalizar(card.dataset.nome).includes(termo) &&
                (isNaN(min) || valor >= min) &&
                (isNaN(max) || valor <= max);
            card.hidden = !passa;
            if (passa) visiveis++;
        });

        [...cards].sort(comparadores[ordem]).forEach((card) => lista.appendChild(card));

        vazio.hidden = visiveis > 0;
        contagem.textContent = visiveis === 1 ? "1 produto" : visiveis + " produtos";
    }

    formBusca.addEventListener("submit", (e) => {
        e.preventDefault();
        atualizar();
    });
    busca.addEventListener("input", atualizar);
    precoMin.addEventListener("input", atualizar);
    precoMax.addEventListener("input", atualizar);
    ordens.forEach((r) => r.addEventListener("change", atualizar));

    limpar.addEventListener("click", () => {
        busca.value = "";
        precoMin.value = "";
        precoMax.value = "";
        document.querySelector('input[name="ordem"][value="padrao"]').checked = true;
        atualizar();
    });

    atualizar();
})();