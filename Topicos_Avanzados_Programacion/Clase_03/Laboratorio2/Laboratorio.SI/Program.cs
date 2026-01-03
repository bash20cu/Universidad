using Laboratorio.BusinessLogicBL;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<Laboratorio.DA.DBContexto>(options =>
    options.UseSqlServer(connectionString));

builder.Services.AddSingleton<IAdministradorDeVehiculos, AdministradorDeVehiculos>();

//builder.Services.AddScoped<Laboratorio.BusinessLogicBL.IVehiculoRepository, Laboratorio.DA.VehiculoRepository>();
//builder.Services.AddScoped<GestionDePersonas.BL.IAdministradorDePersonas, GestionDePersonas.BL.AdministradorDePersonas>();


var app = builder.Build();

//builder.Services.AddDbContext<Laboratorio.DA.DBContexto>(options =>
//    options.UseInMemoryDatabase("PersonasDB"));


// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
