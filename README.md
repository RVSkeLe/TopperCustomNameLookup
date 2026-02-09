# TopperCustomNameLookup

A small addon for the Topper plugin that improves performance by reducing the heavy allocation rates caused by repeated username lookups.

If you want to check how much memory Topper is allocating, you can profile it with:

/spark profiler start --thread * --alloc --timeout 600

Use this to compare allocation usage before and after installing this plugin.

## Screenshots

### Before
![topper_before](https://github.com/user-attachments/assets/548ff617-6d0d-4220-a235-1bea71093a9a)

### After
![topper_after](https://github.com/user-attachments/assets/0fe0e2ad-1217-4a90-93ac-e6c2943ec353)

## Credits

All credits for this Cache solution go to Preva1l
