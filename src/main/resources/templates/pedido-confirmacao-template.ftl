<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-g">
    <title>Confirmação de Pedido</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { width: 90%; max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; }
        .header { background-color: #f4f4f4; padding: 10px; text-align: center; border-bottom: 1px solid #ddd; }
        .content { padding: 20px 0; }
        .footer { font-size: 0.9em; text-align: center; color: #777; margin-top: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f8f8f8; }
        .total { font-weight: bold; text-align: right; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Seu Pedido foi Confirmado!</h2>
        </div>
        <div class="content">
            <h3>Olá, ${nomeCliente}!</h3>
            <p>Obrigado por comprar na Loja Fashion. Recebemos o seu pedido e ele já está sendo processado.</p>
            <p>
                <strong>Número do Pedido:</strong> #${idPedido}<br>
                <strong>Status:</strong> ${statusPedido}
            </p>

            <h4>Resumo da Compra:</h4>
            <table>
                <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Quantidade</th>
                        <th>Preço Unitário</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <#-- Loop para iterar sobre a lista de itens -->
                    <#list itens as item>
                        <tr>
                            <td>${item.produto.nome}</td>
                            <td>${item.quantidade}</td>
                            <td>R$ ${item.precoUnitario?string("0.00")}</td>
                            <td>R$ ${(item.quantidade * item.precoUnitario)?string("0.00")}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>

            <p class="total">
                Valor Total: R$ ${valorTotal?string("0.00")}
            </p>

        </div>
        <div class="footer">
            <p>Em caso de dúvidas, entre em contato com nosso suporte: <a href="mailto:${emailSuporte}">${emailSuporte}</a></p>
            <p>&copy; ${.now?string("yyyy")} Loja Fashion. Todos os direitos reservados.</p>
        </div>
    </div>
</body>
</html>