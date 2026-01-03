using GestionDePersonas.SI;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new() { Title = "Mi API", Version = "v1" });

    c.AddSecurityDefinition("ApiKey", new()
    {
        Description = "Clave de API en el header: X-API-KEY",
        Name = "X-API-KEY",
        In = ParameterLocation.Header,
        Type = SecuritySchemeType.ApiKey,
        Scheme = "ApiKeyScheme"
    });

    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new() { Type = ReferenceType.SecurityScheme, Id = "ApiKey" }
            },
            Array.Empty<string>()
        }
    });
});




//var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

//builder.Services.AddDbContext<GestionDePersonas.DA.DBContexto>(options =>
//    options.UseSqlServer(connectionString));

builder.Services.AddDbContext<GestionDePersonas.DA.DBContexto>(options =>
    options.UseInMemoryDatabase("PersonasDB"));

builder.Services.AddScoped<GestionDePersonas.BL.IPersonaRepository, GestionDePersonas.DA.PersonaRepository>();
builder.Services.AddScoped<GestionDePersonas.BL.IAdministradorDePersonas, GestionDePersonas.BL.AdministradorDePersonas>();


var app = builder.Build();

//// Configure the HTTP request pipeline.
//if (app.Environment.IsDevelopment())
//{
    app.UseSwagger();
    app.UseSwaggerUI();
//}


app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();


//app.UseMiddleware<ApiKeyMiddleware>();

app.Run();
