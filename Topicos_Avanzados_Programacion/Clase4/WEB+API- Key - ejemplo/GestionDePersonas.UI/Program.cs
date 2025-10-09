
using GestionDePersonas.UI;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();
builder.Services.AddHttpClient("PersonasApi", client =>
{
    client.BaseAddress = new Uri("https://localhost:7119/");
    client.DefaultRequestHeaders.Add("X-API-KEY", "123456");
});
builder.Services.AddScoped<ServicioApi>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=GestionDePersonas}/{action=Index}/{id?}");

app.Run();
