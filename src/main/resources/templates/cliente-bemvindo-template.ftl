<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Bem-vindo(a) à Loja Fashion!</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { width: 80%; margin: 0 auto; padding: 20px; border: 1px solid #ddd; }
        .header { background-color: #f0f0f0; padding: 10px; text-align: center; }
        .content { padding: 20px 0; }
        .footer { font-size: 0.9em; text-align: center; color: #777; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Bem-vindo(a) à Loja Fashion, ${nomeCliente}!</h1>
        </div>
        <div class="content">
            <p>Olá, ${nomeCliente},</p>
            <p>Sua conta em nossa Loja Fashion foi criada com sucesso!</p>
            <p>Estamos muito felizes em ter você conosco. Explore nossa coleção e aproveite as últimas tendências da moda.</p>
            <p>Seu ID de cliente é: <strong>${idCliente}</strong>.</p>
            <p>Para acessar sua conta ou começar a comprar, visite nosso site.</p>
            <p>Se você não criou esta conta, por favor, ignore este e-mail ou entre em contato com nosso suporte em ${emailSuporte}.</p>
            <br>
            <p>Atenciosamente,</p>
            <p>Equipe Loja Fashion</p>
        </div>
        <div class="footer">
            <p>&copy; ${.now?string("yyyy")} Loja Fashion. Todos os direitos reservados.</p>
            <p>Este é um e-mail automático, por favor, não responda.</p>
        </div>
    </div>
</body>
</html>